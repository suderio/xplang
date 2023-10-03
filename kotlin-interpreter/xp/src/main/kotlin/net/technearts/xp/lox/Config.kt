package net.technearts.xp.lox

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithName


@ConfigMapping(prefix = "xp")
interface Config {

    @WithName("message")
    fun message(): String?

}