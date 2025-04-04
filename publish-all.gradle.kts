tasks.register("publishAllMedia3Sequentially") {
    group = "publishing"
    description = "Publishes selected media3 modules to GitHub Packages."

    val media3Modules = listOf(
        "common",
        "common_ktx",
        "container",
        "decoder",
        "datasource",
        "datasource_cronet",
        "datasource_okhttp",
        "datasource_rtmp",
        "exoplayer",
        "exoplayer_dash",
        "exoplayer_hls",
        "exoplayer_ima",
        "exoplayer_rtsp",
        "exoplayer_smoothstreaming",
        "exoplayer_workmanager",
        "extractor",
        "muxer",
        "session",
        "transformer",
        "ui",
        "ui_compose",
        "ui_leanback"
    )

    doLast {
        println("✅ Found ${media3Modules.size} modules to publish:")
        media3Modules.forEach { println("  → media3:$it") }

        val success = mutableListOf<String>()
        val failed = mutableMapOf<String, Int>()

        media3Modules.forEach { module ->
            val task = ":media3:lib-$module:publishReleasePublicationToGitHubPackagesRepository"
            println("\n🚀 Running $task ...")

            val result = exec {
                isIgnoreExitValue = true
                commandLine("cmd", "/c", "gradlew.bat", task)
            }

            if (result.exitValue == 0) {
                println("✅ $module published successfully")
                success.add(module)
            } else {
                println("❌ $module failed to publish (exit code ${result.exitValue})")
                failed[module] = result.exitValue
            }
        }

        println("\n📦 PUBLISH SUMMARY")
        println("✅ Published (${success.size}):")
        success.forEach { println("  - $it") }

        println("\n❌ Failed (${failed.size}):")
        failed.forEach { (mod, code) ->
            println("  - $mod (exit code: $code)")
        }
    }
}