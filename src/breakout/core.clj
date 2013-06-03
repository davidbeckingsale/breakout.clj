(ns breakout.core
  (:use [breakout.world :only (screen-size)]
        [breakout.components :only (game position renderable texture tag)]
        [breakout.levels.one :as level]
        [breakout.lib.core :only (all-e add get-e)]
        [breakout.lib.macros :only (?)])
  (:require [breakout.systems.states :as states]
            [breakout.ui.menu :as menu])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn init []
  (let [[x y] screen-size]
    (Display/setDisplayMode (DisplayMode. x y))
    (Display/setTitle "Breakout!")
    (Display/create)
    (GL11/glEnable GL11/GL_TEXTURE_2D)
    (GL11/glClearColor 0.0 0.0 0.0 0.0)
    (GL11/glEnable GL11/GL_BLEND)
    (GL11/glViewport 0 0 x y)
    (GL11/glMatrixMode GL11/GL_MODELVIEW)
    (GL11/glMatrixMode GL11/GL_PROJECTION)
    (GL11/glLoadIdentity)
    (GL11/glOrtho 0 x y 0 1 -1)
    (GL11/glMatrixMode GL11/GL_MODELVIEW)
    (add (game :menu 0))))


(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [g (init)]
        (menu/menu)
    (while (and (not (Display/isCloseRequested))
                (not= (? (get-e g) :game :state) :exit))
      (case (? (get-e g) :game :state)
        :menu
        (states/menu)
      :running
        (states/running)
       :pause
       (states/pause)))
    (finalise)))
