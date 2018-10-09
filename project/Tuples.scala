import sbt._

object Tuples {
  def generate(basedir: File, pkg: String): IndexedSeq[File] = {
    val f = basedir / pkg.replace('.', '/')

    for(n <- 2 to 27) yield {
      val file = f / s"Tuple$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"_${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2)
      val curried = params.map(s => s"Function<${s._1}").mkString(", ") + ", B" + ">" * params.size

      def _2 =
        s"""  public Tuple2<A2, A1> swap(){
           |    return new Tuple2<>(_2, _1);
           |  }""".stripMargin

      val content =
        s"""package $pkg;
           |
           |import java.io.Serializable;
           |import java.util.Objects;
           |import java.util.function.Function;
           |
           |public final class Tuple$n<$types> implements Serializable {
           |  ${params.map { case (a, i) => s"public final $a $i;" }.mkString("\n  ")}
           |
           |  public Tuple$n(${params.map { case (a, i) => s"$a $i" }.mkString(", ")}){
           |    ${params.map { case (a, i) => s"this.$i = $i;" }.mkString("\n    ")}
           |  }
           |
           |${if (n == 2) _2 else ""}
           |
           |  public static <$types, B> $curried curry(Function<Tuple$n<$types>, B> f){
           |    return ${names.mkString(" -> ")} -> f.apply(new Tuple$n<>(${names.mkString(", ")}));
           |  }
           |
           |  public static <$types, B> Function<Tuple$n<$types>, B> uncurry($curried f){
           |    return t -> f${names.map(a => s".apply(t.$a)").mkString("")};
           |  }
           |
           |  @Override
           |  public int hashCode() {
           |    return Objects.hash(${names.mkString(", ")});
           |  }
           |
           |  @Override
           |  public boolean equals(Object obj) {
           |    if(obj == this){
           |      return true;
           |    } else if(obj instanceof Tuple$n){
           |      Tuple$n other = (Tuple$n)obj;
           |      return ${names.map(n => s"Objects.equals($n, other.$n)").mkString(" && ")};
           |    } else return false;
           |  }
           |
           |  @Override
           |  public String toString(){
           |    return "(" + ${names.mkString(" + \", \" + ")} + ")";
           |  }
           |}
      """.stripMargin
      IO.write(file, content)
      file
    }
  }

  def factory(basedir: sbt.File, pkg: String): IndexedSeq[File] = {
    val f = basedir / pkg.replace('.', '/')

    val methods = for(n <- 2 to 27) yield {
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"_${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")
      s"""
         |  public static <$types> Tuple$n<$types> of($paramsString) {
         |    return new Tuple$n<>($names);
         |  }
       """.stripMargin
    }

    val content =
      s"""package $pkg;
         |public final class Tuples {
         |  private Tuples() {
         |  }
         |${methods.mkString("\n")}
         |}
         |
      """.stripMargin
    val file = f / "Tuples.java"
    IO.write(file, content)
    Vector(
      file
    )
  }
}
