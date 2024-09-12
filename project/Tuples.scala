import sbt._

object Tuples {
  def generate(baseDir: File, sourceDir: File, pkg: String): IndexedSeq[File] = {
    val f = sourceDir / pkg.replace('.', '/')
    val serialVersionUID = {SerialVersionUID.shaAsLong(baseDir / "project" / "Tuples.scala")}

    def append(n: Int, types: String, names: Seq[String]) = {
      val next = n+1
      s"""  public <A$next> Tuple$next<$types, A$next> append(A$next _$next) {
         |    return Tuples.of(${names.mkString(", ")}, _$next);
         |  }
         |
         |  public <A0> Tuple$next<A0, $types> prepend(A0 first) {
         |    return Tuples.of(first, ${names.mkString(", ")});
         |  }""".stripMargin
    }

    for(n <- 2 to 27) yield {
      val file = f / s"Tuple$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"_${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2)
      val curried = params.map(s => s"Function<${s._1}").mkString(", ") + ", B" + ">" * params.size

      def _2 =
        s"""  public Tuple2<A2, A1> swap(){
           |    return new Tuple2<>(_2, _1);
           |  }
           |
           |  public java.util.Map.Entry<A1, A2> asEntry() {
           |    return new java.util.AbstractMap.SimpleImmutableEntry<>(_1, _2);
           |  }""".stripMargin

      val content =
        s"""package $pkg;
           |
           |import java.util.Objects;
           |import java.util.function.Function;
           |import java.util.List;
           |import java.util.Arrays;
           |
           |public record Tuple$n<$types>(${params.map { case (a, i) => s"$a $i" }.mkString(", ")}) {
           |
           |${if (n < 27) append(n, types, names) else ""}
           |${if (n == 2) _2 else ""}
           |
           |  public static <$types, B> $curried curry(Function<Tuple$n<$types>, B> f) {
           |    return ${names.mkString(" -> ")} -> f.apply(new Tuple$n<>(${names.mkString(", ")}));
           |  }
           |
           |  public static <$types, B> Function<Tuple$n<$types>, B> uncurry($curried f) {
           |    return t -> f${names.map(a => s".apply(t.$a)").mkString("")};
           |  }
           |
           |${params.zipWithIndex.map{case ((a, field), idx) => s"  public $a component${idx + 1}() {\n    return $field;\n  }\n"}.mkString("\n")}
           |
           |${params.zipWithIndex.map{case ((a, field), idx) => s"  public <B> Tuple$n<${params.map(_._1).updated(idx, "B").mkString(", ")}> map${idx + 1}(Function<? super $a, ? extends B> f) {\n    return new Tuple$n<>(${names.updated(idx, s"f.apply($field)").mkString(", ")});\n  }\n"}.mkString("\n")}
           |
           |
           |  public int arity() {
           |    return $n;
           |  }
           |
           |  public List<Object> asList() {
           |    return Arrays.asList(${names.mkString(", ")});
           |  }
           |
           |  @Override
           |  public String toString() {
           |    return asList().stream().map(Object::toString).collect(java.util.stream.Collectors.joining(", ", "(", ")"));
           |  }
           |
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
