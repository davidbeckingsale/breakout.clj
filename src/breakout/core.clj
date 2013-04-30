(ns breakout.core
  (:use [breakout.world :only (screen-size)]
        [breakout.levels.one :as one]
        ; [breakout.levels.test :as testlevel]
        [breakout.systems.camera :as camera]
        [breakout.systems.moveable :as move]
        [breakout.lib.physics :as phys]
        [breakout.lib.core :only (all-e)])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn init []
  (let [[x y] screen-size]
    (Display/setDisplayMode (DisplayMode. x y))
    (Display/setTitle "Breakout!")
    (Display/create)

    (GL11/glMatrixMode GL11/GL_PROJECTION)
    (GL11/glLoadIdentity)
    (GL11/glOrtho 0 x y 0 1 -1)
    (GL11/glMatrixMode GL11/GL_MODELVIEW)))

(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (init)
  (one/level)
  (while (not (Display/isCloseRequested))
    (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
    (camera/renderer (all-e :renderable))
    (move/keyboard (all-e :keyboard))
    (move/move (all-e :paddle-actions))
    (phys/step)
    (Display/update)
    (Display/sync 60))
  (Display/destroy))
