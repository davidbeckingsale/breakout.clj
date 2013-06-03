(ns breakout.systems.states
  (:use [breakout.levels.one :as level]
        [breakout.lib.physics :as phys]
        [breakout.lib.core :only (all-e add get-e)])
  (:require [breakout.systems.moveable :as move]
            [breakout.systems.ui :as ui]
            [breakout.systems.score :as score]
            [breakout.systems.camera :as camera])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn menu [] 
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (camera/renderer (all-e :renderable))
  (ui/keyboard (all-e :game))
  (Display/update)
  (Display/sync 60))

(defn running []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (camera/renderer (all-e :renderable))
  (ui/keyboard (all-e :game))
  (move/keyboard (all-e :keyboard))
  (move/move (all-e :paddle-actions))
  (phys/step)
  (score/update (all-e :destroyed?))
  (Display/update)
  (Display/sync 60))

(defn pause []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (camera/renderer (all-e :renderable))
  (ui/keyboard (all-e :game))
  (Display/update)
  (Display/sync 60))
