package hackman.trevor.copycat.system.sound

// Expose minimal interface!
interface Sound {
    fun play()
}

object NullSound: Sound {
    override fun play() {}
}
