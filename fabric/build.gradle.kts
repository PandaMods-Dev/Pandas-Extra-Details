// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

val modmenuVersion: String by project
val sodiumVersion: String by project
val nvidiumVersion: String by project
val irisVersion: String by project

architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

	modApi("com.terraformersmc:modmenu:${modmenuVersion}")

	modCompileOnly("maven.modrinth:sodium:${sodiumVersion}")
	modRuntimeOnly("maven.modrinth:sodium:${sodiumVersion}")

//	modCompileOnly("maven.modrinth:iris:${irisVersion}")
//	modRuntimeOnly("maven.modrinth:iris:${irisVersion}")

//	modCompileOnly("maven.modrinth:nvidium:${nvidiumVersion}")
//	modRuntimeOnly("maven.modrinth:nvidium:${nvidiumVersion}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionFabric"))
}


tasks.remapJar {
	injectAccessWidener.set(true)
}