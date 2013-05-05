(ns breakout.core
  (:use [breakout.world :only (screen-size)]
        [breakout.components :only (game)]
        [breakout.levels.one :as level]
        ; [breakout.levels.harry :as level]
        ; [breakout.levels.test :as testlevel]
        [breakout.systems.camera :as camera]
        [breakout.lib.physics :as phys]
        [breakout.lib.core :only (all-e add get-e)]
        [breakout.lib.macros :only (?)])
  (:require [breakout.systems.moveable :as move]
            [breakout.systems.ui :as ui]
            [breakout.systems.score :as score])
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
    (add (game :running 0))))

(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [game (init)]
    (level/level)
    (while (not= (? (get-e game) :game :state) :exit)
      (while (and (not (Display/isCloseRequested))
                  (= (? (get-e game) :game :state) :running))
        (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
        (camera/renderer (all-e :renderable))
        (ui/keyboard (all-e :game))
        (move/keyboard (all-e :keyboard))
        (move/move (all-e :paddle-actions))
        (phys/step)
        (score/update (all-e :destroyed?))
        (Display/update)
        (Display/sync 60)))
    (finalise)))
