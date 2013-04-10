(ns breakout.core
  (:use [breakout.world :only (screen-size random-world)]
        [breakout.physics :only (update-physics)]
        [breakout.lib.draw :only (draw)]
        [breakout.lib.input :only (update-input)])
  (:import (org.lwjgl.opengl Display DisplayMode GL11)))

(defn init []
  (let [[x y] screen-size]
    (Display/setDisplayMode (DisplayMode. x y))
    (Display/setTitle "Breakout!")
    (Display/create)

    (GL11/glMatrixMode GL11/GL_PROJECTION)
    (GL11/glLoadIdentity)
    (GL11/glOrtho 0 x y 0 1 -1)
    (GL11/glMatrixMode GL11/GL_MODELVIEW))
  (random-world))

(defn update [world]
  (-> world
      (update-input)
      (update-physics)))

(defn render-loop [w]
  (loop [world w]
    (when (and (:running world)
               (not (Display/isCloseRequested)))
      (draw world)
      (recur (update world)))))

(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [world (init)]
    (render-loop world)
    (finalise)))
