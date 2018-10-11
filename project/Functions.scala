import sbt._

object Functions {
  def generate(basedir: File, sourceDir: File, pkg: String): IndexedSeq[File] = {
    val f = sourceDir / pkg.replace('.', '/')

    for(n <- 3 to 27) yield {
      val file = f / s"Function$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"p${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")
      val tupledValues = (1 to n).map(i => s"t._$i").mkString(", ")
      val curried = params.map(_._2).mkString(" -> ")
      val curriedTypes = params.map{case (tpe, _) => s"Function<$tpe"}.mkString(", ") + ", B" + (">" * n)

      val content =
        s"""package $pkg;
           |
           |import java.util.function.Function;
           |import java.util.Objects;
           |import java.io.Serializable;
           |
           |@FunctionalInterface
           |public interface Function$n<$types, B> extends Serializable {
           |   B apply($paramsString);
           |
           |   static <$types, B> Function$n<$types, B> constant(B value) {
           |     return ($names) -> value;
           |   }
           |
           |   static <$types, B> Function$n<$types, B> untupled(Function<Tuple$n<$types>, B> f) {
           |     return ($names) -> f.apply(Tuples.of($names));
           |   }
           |
           |   default Function<Tuple$n<$types>, B> tupled() {
           |      return t -> apply($tupledValues);
           |   }
           |
           |   default $curriedTypes curried() {
           |      return $curried -> apply($names);
           |   }
           |
           |   default <V> Function$n<$types, V> andThen(Function<? super B, ? extends V> after) {
           |     Objects.requireNonNull(after, "after is null");
           |     return ($names) -> after.apply(apply($names));
           |   }
           |
           |   long serialVersionUID = ${SerialVersionUID.shaAsLong(basedir / "project" / "Functions.scala")}L;
           |}
           |
      """.stripMargin
      IO.write(file, content)
      file
    }
  }
}
