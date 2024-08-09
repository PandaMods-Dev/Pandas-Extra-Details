// gradle.properties
val modId: String by project

val minecraftVersion: String by project
val fabricLoaderVersion: String by project

val sodiumVersion: String by project
val embeddiumVersion: String by project
val nvidiumVersion: String by project

val parchmentVersion: String by project
val parchmentMinecraftVersion: String by project

val supportedModLoaders: String by project

architectury {
	common(supportedModLoaders.split(","))
}

loom.accessWidenerPath.set(file("src/main/resources/${modId}.accesswidener"))

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")

	modImplementation("maven.modrinth:sodium:${sodiumVersion}")
//    modImplementation("maven.modrinth:embeddium:${rootProject.embeddiumVersion}")

	modImplementation("maven.modrinth:nvidium:${nvidiumVersion}")
}