// gradle.properties
val neoForgeVersion: String by project
val embeddiumVersion: String by project

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"], configurations["jarShadow"])
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

dependencies {
	neoForge("net.neoforged:neoforge:${neoForgeVersion}")

//	modImplementation("maven.modrinth:embeddium:${embeddiumVersion}-neoforge")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowCommon"(project(":common", "transformProductionNeoForge")) { isTransitive = false }
}

tasks {
	base.archivesName.set(base.archivesName.get() + "-neoforge")

	shadowJar {
		exclude("fabric.mod.json")
	}

	remapJar {
		injectAccessWidener = true
		atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
	}
}