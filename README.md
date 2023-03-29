# Quick Guide for Enterprise Application Development

This repository contains a set of sample applications based on the Quarkus framework.

While the coding is simple, we advise to keep referring to the official [Quarkus guide](https://quarkus.io/guides/)
for proper guidance on how to
use the different tools used here.

## Requirements

To compile and run this demo you will need:

- JDK 11+
- GraalVM

In addition, you will need either a PostgreSQL database, or Docker to run one.

### Configuring GraalVM and JDK 11+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 11+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Use alternative platforms

These applications by default currently uses the Quarkus core BOM.

## Contributions

See [CONTRIBUTING](CONTRIBUTING.md) for how to build these examples.

## Application List

* [Authorization OIDC](./authorization): Authorization Application.
* [Inventory](./inventory): Inventory Application.

Have fun, and join the team of contributors!
