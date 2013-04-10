(ns breakout.lib.draw
  (:use [breakout.world :only (screen-size)])
  (:import (org.lwjgl.opengl Display GL11)))

(def PI 3.14159)
(def -PI -3.14159)

(defn draw-brick [brick]
  (let [[screen-x screen-y] screen-size
        [x y] (:position brick)
        [r g b] (:colour brick)]
    (if-not (:destroyed brick)
      (do (GL11/glColor3f r g b)
          (GL11/glBegin GL11/GL_QUADS)
          (GL11/glVertex2f x y)
          (GL11/glVertex2f (+ x 100) y)
          (GL11/glVertex2f (+ x 100) (+ y 60))
          (GL11/glVertex2f x (+ y 60))
          (GL11/glEnd)))))

(defn draw-row [row]
  (doall (map draw-brick row)))

(defn draw-world [world]
  (let [bricks (:bricks world)]
    (doall (map draw-row bricks))))

(defn draw-paddle [paddle]
  (let [[x y] (:position paddle)
        [dx dy] (:velocity paddle)]
    (GL11/glColor3f 1.0 0.0 0.0)

    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x 100) y)
    (GL11/glVertex2f (+ x 100) (+ y 20))
    (GL11/glVertex2f x (+ y 20))
    (GL11/glEnd)))

(defn draw-ball [ball]
  (let [[x y] (:position ball)
        r 5
        s 100
        ss (/ (* PI 2) s)]
    (GL11/glColor3f 1.0 0.0 0.0)
    (GL11/glBegin GL11/GL_POLYGON)
    (doseq [i (filter (fn [x] (>= x -PI)) (map (fn [y] (- PI (* y ss))) (take s (range))))]
      (GL11/glVertex2f (+ x (* (Math/cos i) r)) (+ y (* (Math/sin i) r))))
    (GL11/glEnd)))

(defn draw [world]
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
  (let [paddle (:paddle world)
        ball (:ball world)]
    (draw-world world)
    (draw-ball ball)
    (draw-paddle paddle))
  (Display/update)
  (Display/sync 60))
