load("//tools/bzl:junit.bzl", "junit_tests")
load(
    "//tools/bzl:plugin.bzl",
    "PLUGIN_DEPS",
    "PLUGIN_TEST_DEPS",
    "gerrit_plugin",
)

gerrit_plugin(
    name = "poc-ai-review",
    srcs = glob(["src/main/java/**/*.java"]),
    manifest_entries = [
        "Gerrit-PluginName: poc-ai-review",
        "Gerrit-Module: com.poc.gerrit.plugins.aireview.Module",
        "Implementation-Title: poc-ai-review Plugin",
    ],
    resources = glob(["src/main/resources/Documentation/*.md"]),
    deps = [
    ],
)

junit_tests(
    name = "poc-ai-review_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    tags = ["poc-ai_review"],
    deps = [
        ":poc-ai-review__plugin_test_deps",
    ],
)

java_library(
    name = "poc-ai-review__plugin_test_deps",
    testonly = 1,
    visibility = ["//visibility:public"],
    exports = PLUGIN_DEPS + PLUGIN_TEST_DEPS + [
        ":poc-ai-review__plugin",
        "@commons-io//jar",
        "@mockito//jar",
    ],
)
