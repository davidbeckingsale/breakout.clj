(ns breakout.core
    (:import (org.lwjgl LWJGLException) (org.lwjgl.opengl Display DisplayMode)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (Display/setDisplayMode (DisplayMode. 800 600))
  (Display/setTitle "Breakout!")
  (Display/create)
  (while (not (Display/isCloseRequested))
         (Display/update)
         (Display/sync 60)
         )
  (Display/destroy))
