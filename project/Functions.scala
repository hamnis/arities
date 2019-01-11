import sbt._

object Functions {
  def generate(basedir: File, sourceDir: File, pkg: String): IndexedSeq[File] = {
    val f = sourceDir / pkg.replace('.', '/')
    val serialVersionUID = SerialVersionUID.shaAsLong(basedir / "project" / "Functions.scala")

    for(n <- 3 to 27) yield {
      val file = f / s"Function$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"p${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")
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
           |       return ($names) -> value;
           |   }
           |
           |   static <$types, B> Function$n<$types, B> untupled(Function<Tuple$n<$types>, B> f) {
           |       return ($names) -> f.apply(Tuples.of($names));
           |   }
           |
           |   default Function<Tuple$n<$types>, B> tupled() {
           |       return t -> apply(${(1 to n).map(i => s"t._$i").mkString(", ")});
           |   }
           |
           |   default $curriedTypes curried() {
           |       return ${params.map(_._2).mkString(" -> ")} -> apply($names);
           |   }
           |
           |   default <V> Function$n<$types, V> andThen(Function<? super B, ? extends V> after) {
           |       Objects.requireNonNull(after, "after is null");
           |       return ($names) -> after.apply(apply($names));
           |   }
           |
           |   long serialVersionUID = ${serialVersionUID}L;
           |}
           |
      """.stripMargin
      IO.write(file, content)
      file
    }
  }

  def generateException(basedir: File, sourceDir: File, pkg: String, prefix:String, exception: String): IndexedSeq[File] = {
    val f = sourceDir / pkg.replace('.', '/')
    val serialVersionUID = SerialVersionUID.shaAsLong(basedir / "project" / "Functions.scala")

    for(n <- 3 to 27) yield {
      val file = f / s"${prefix}Function$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"p${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")
      val curriedTypes = params.map{case (tpe, _) => s"${prefix}Function<$tpe"}.mkString(", ") + ", B" + (">" * n)
      val exceptionBaseType = exception.substring(exception.lastIndexOf('.') +1)

      val content =
        s"""package $pkg;
           |
           |import java.util.function.Function;
           |import java.util.Objects;
           |import java.io.Serializable;
           |import $exception;
           |
           |@FunctionalInterface
           |public interface ${prefix}Function$n<$types, B> extends Serializable {
           |   B apply($paramsString) throws $exceptionBaseType;
           |
           |   static <$types, B> ${prefix}Function$n<$types, B> constant(B value) {
           |       return ($names) -> value;
           |   }
           |
           |   static <$types, B> ${prefix}Function$n<$types, B> untupled(${prefix}Function<Tuple$n<$types>, B> f) {
           |       return ($names) -> f.apply(Tuples.of($names));
           |   }
           |
           |   default ${prefix}Function<Tuple$n<$types>, B> tupled() {
           |       return t -> apply(${(1 to n).map(i => s"t._$i").mkString(", ")});
           |   }
           |
           |   static <$types, B> ${prefix}Function$n<$types, B> fromFunction(Function$n<$types, B> f) {
           |       return ($names) -> Sneaky.getOrThrowE(
           |               () -> f.apply($names),
           |               $exceptionBaseType.class,
           |               $exceptionBaseType::new
           |       );
           |   }
           |
           |   default Function$n<$types, B> unchecked() {
           |       return ($names) -> {
           |           try {
           |               return apply($names);
           |           } catch ($exceptionBaseType e) {
           |               throw Sneaky.sneakyThrow(e);
           |           }
           |      };
           |   }
           |
           |   default $curriedTypes curried() {
           |      return ${params.map(_._2).mkString(" -> ")} -> apply($names);
           |   }
           |
           |   default <V> ${prefix}Function$n<$types, V> andThen(${prefix}Function<? super B, ? extends V> after) {
           |     Objects.requireNonNull(after, "after is null");
           |     return ($names) -> after.apply(apply($names));
           |   }
           |
           |   long serialVersionUID = ${serialVersionUID}L;
           |}
           |
      """.stripMargin
      IO.write(file, content)
      file
    }
  }
}
