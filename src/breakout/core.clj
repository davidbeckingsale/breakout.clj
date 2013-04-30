(ns breakout.core
  (:use [breakout.world :only (screen-size)]
        [breakout.components :only (state)]
        [breakout.levels.one :as one]
        ; [breakout.levels.test :as testlevel]
        [breakout.systems.camera :as camera]
        [breakout.lib.physics :as phys]
        [breakout.lib.core :only (all-e add)])
  (:require [breakout.systems.moveable :as move]
            [breakout.systems.ui :as ui])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn init []
  (let [[x y] screen-size]
    (Display/setDisplayMode (DisplayMode. x y))
    (Display/setTitle "Breakout!")
    (Display/create)

    (GL11/glMatrixMode GL11/GL_PROJECTION)
    (GL11/glLoadIdentity)
    (GL11/glOrtho 0 x y 0 1 -1)
    (GL11/glMatrixMode GL11/GL_MODELVIEW)
    (add (state :running))))

(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (init)
  (while (not= (first (all-e :state)) :quit)
    (println (first (all-e :state)))
    (one/level)
    (while (and (not (Display/isCloseRequested))
                (= (:state (first (all-e :state))) :running))
      (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
      (camera/renderer (all-e :renderable))
      (ui/keyboard (all-e :state))
      (move/keyboard (all-e :keyboard))
      (move/move (all-e :paddle-actions))
      (phys/step)
      (Display/update)
      (Display/sync 60)))
  (Display/destroy))
