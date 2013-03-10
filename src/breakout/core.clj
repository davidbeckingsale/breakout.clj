(ns breakout.core
    (:import (org.lwjgl LWJGLException)
     (org.lwjgl.opengl Display DisplayMode GL11)))

(def world-size [4 2])
(def screen-size [800 600])

(defrecord World [bricks])
(defrecord Brick [colour destroyed])

(def bricks
     {:red (->Brick [1.0 0.0 0.0] nil)
     :green (->Brick [0.0 1.0 0.0] nil)
     :blue (->Brick [0.0 0.0 1.0] nil)
     :orange (->Brick [1.0 0.5 0.0] nil)
     :purple (->Brick [1.0 0.0 1.0] nil)
     :violet (->Brick [0.0 1.0 1.0] nil)})

(defn random-bricks []
  (let [[cols rows] world-size]
    (letfn [(random-brick [] (bricks (rand-nth [:red :green :blue :orange :purple :violet])))
            (random-row [] (vec (repeatedly cols random-brick)))]
           (vec (repeatedly rows random-row)))))

(defn draw-brick [brick location]
  (let [[screen-x screen-y] screen-size
        [x y] location
        [r g b] (:colour brick)]
    (GL11/glColor3f r g b)

    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x 200) y)
    (GL11/glVertex2f (+ x 200) (+ y 150))
    (GL11/glVertex2f x (+ y 150))
    (GL11/glEnd)))

(defn random-world []
  (->World (random-bricks)))

(defn draw-world [world]
  (let [[x y] world-size
        [gx gy] screen-size
        bx 200
        by 150]
    (doseq [row (range 0 y)]
      (doseq [col (range 0 x)]
        (draw-brick (nth (nth (:bricks world) row) col)
                    [(* col bx) (* row by)])))))

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

(defn render-loop [world]
  (while (not (Display/isCloseRequested))
    (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
    (draw-world world)
    (Display/update)
    (Display/sync 60)))

(defn finalise []
  (Display/destroy))

(defn -main
  "I don't do a whole lot."
  [& args]
  (let [world (init)]
    (render-loop world)
    (finalise)))
