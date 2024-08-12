// gradle.properties
val modId: String by project

val forgeVersion: String by project
val embeddiumVersion: String by project

architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)

	forge {
		convertAccessWideners.set(true)
		extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

		// Fixes Mixin Patcher issue with Forge
		useCustomMixin.set(false)

		mixinConfig("${modId}-common.mixins.json")
		mixinConfig("${modId}.mixins.json")
	}
}

configurations {
	getByName("developmentForge").extendsFrom(configurations["common"], configurations["jarShadow"])
}

dependencies {
	forge("net.minecraftforge:forge:${forgeVersion}")

	modImplementation("maven.modrinth:embeddium:${embeddiumVersion}-forge")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowCommon"(project(":common", "transformProductionForge")) { isTransitive = false }
}

tasks {
	base.archivesName.set(base.archivesName.get() + "-forge")

	shadowJar {
		exclude("fabric.mod.json")
	}

	remapJar {
		injectAccessWidener = true
	}
}