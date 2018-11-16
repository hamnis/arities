import sbt._

object Consumers {
  def generate(basedir: File, sourceDir: File, pkg: String): IndexedSeq[File] = {
    val f = sourceDir / pkg.replace('.', '/')
    val serialVersionUID = SerialVersionUID.shaAsLong(basedir / "project" / "Consumers.scala")

    for(n <- 3 to 27) yield {
      val file = f / s"Consumer$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"p${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")

      val content =
        s"""package $pkg;
           |
           |import java.util.function.Consumer;
           |import java.util.Objects;
           |import java.io.Serializable;
           |
           |@FunctionalInterface
           |public interface Consumer$n<$types> extends Serializable {
           |   void accept($paramsString);
           |
           |   default Consumer<Tuple$n<$types>> tupled() {
           |      return t -> accept(${(1 to n).map(i => s"t._$i").mkString(", ")});
           |   }
           |
           |   default Function$n<$types, Void> asFunction() {
           |      return ($names) -> {
           |        accept($names);
           |        return null;
           |      };
           |   }
           |
           |   static <$types> Consumer$n<$types> untupled(Consumer<Tuple$n<$types>> c) {
           |      return ($names) -> c.accept(Tuples.of($names));
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
    val serialVersionUID = SerialVersionUID.shaAsLong(basedir / "project" / "Consumers.scala")

    for(n <- 3 to 27) yield {
      val file = f / s"${prefix}Consumer$n.java"
      val params = List.tabulate(n)(i => s"A${i + 1}" -> s"p${i + 1}")
      val types = params.map(_._1).mkString(", ")
      val names = params.map(_._2).mkString(", ")
      val paramsString = params.map{case (tpe, name) => s"$tpe $name"}.mkString(", ")
      val exceptionBaseType = exception.substring(exception.lastIndexOf('.') +1)

      val content =
        s"""package $pkg;
           |
           |import java.util.function.Consumer;
           |import java.util.Objects;
           |import java.io.Serializable;
           |import $exception;
           |
           |@FunctionalInterface
           |public interface ${prefix}Consumer$n<$types> extends Serializable {
           |   void accept($paramsString) throws $exceptionBaseType;
           |
           |   static <$types> ${prefix}Consumer$n<$types> untupled(Consumer<Tuple$n<$types>> f) {
           |     return ($names) -> f.accept(Tuples.of($names));
           |   }
           |
           |   default ${prefix}Consumer<Tuple$n<$types>> tupled() {
           |      return t -> accept(${(1 to n).map(i => s"t._$i").mkString(", ")});
           |   }
           |
           |   default ${prefix}Function$n<$types, Void> asFunction() {
           |      return ($names) -> {
           |        accept($names);
           |        return null;
           |      };
           |   }
           |
           |   default Consumer$n<$types> unchecked() {
           |      return ($names) -> {
           |        try {
           |          accept($names);
           |        } catch ($exceptionBaseType e) {
           |          throw Sneaky.sneakyThrow(e);
           |        }
           |      };
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
