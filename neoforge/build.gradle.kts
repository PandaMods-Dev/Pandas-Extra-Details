// gradle.properties
val neoForgeVersion: String by project
val embeddiumVersion: String by project

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}


dependencies {
	neoForge("net.neoforged:neoforge:${neoForgeVersion}")

//	modImplementation("maven.modrinth:embeddium:${embeddiumVersion}-neoforge")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionNeoForge"))
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}