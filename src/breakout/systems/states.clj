(ns breakout.systems.states
  (:use [breakout.levels.one :as level]
        [breakout.lib.physics :as phys]
        [breakout.lib.core :only (all-e add get-e tagged-e)])
  (:require [breakout.systems.moveable :as move]
            [breakout.systems.score :as score]
            [breakout.systems.camera :as camera]
            [breakout.systems.input :as input])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn menu [] 
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (input/keyboard (all-e :game))
  (camera/renderer (all-e :renderable))
  (Display/update)
  (Display/sync 60))

(defn running []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (input/keyboard (all-e :keyboard))
  (camera/renderer (all-e :renderable))
  (move/move (all-e :paddle-actions))
  (phys/step)
  (score/update (all-e :destroyed?))
  (score/lives (tagged-e :ball))
  (Display/update)
  (Display/sync 60))

(defn paused []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (input/keyboard (all-e :game))
  (camera/renderer (all-e :renderable))
  (Display/update)
  (Display/sync 60))

(defn lose []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (input/keyboard (all-e :game))
  (camera/renderer (all-e :renderable))
  (Display/update)
  (Display/sync 60))

(defn win []
  (GL11/glClear (GL11/GL_COLOR_BUFFER_BIT))
  (input/keyboard (all-e :game))
  (camera/renderer (all-e :renderable))
  (Display/update)
  (Display/sync 60))
