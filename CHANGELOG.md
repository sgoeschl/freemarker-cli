# Change Log

All notable changes to this project will be documented in this file. We try to adhere to https://github.com/olivierlacan/keep-a-changelog.

## 2.0.0-BETA-2-SNAPSHOT

### Added
- Add "-E" command line option to expose all environment variable as top-level entry in the data model 
- Support for [Grok](https://github.com/thekrakken/java-grok) as better alternative for regular expressions
- Support for streaming textual content over `System.in` to allow processing of arbitrary large source files
- Display Git version information when using `-V` command line option

## [2.0.0-BETA-1] - 2019-10-12

### Added
- Support for YAML files using SnakeYAML

### Changed
- Upgraded dependencies
- Migrated from Groovy to plain old JDK 1.8
- Works as standalone application with OS wrapper scripts
- Existing templates will break due to naming changes of injected helper classes

[2.0.0-BETA-1]: https://github.com/sgoeschl/freemarker-cli/releases/tag/v2.0.0-BETA-1
