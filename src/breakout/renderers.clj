(ns breakout.renderers
  (:import (org.lwjgl.opengl Display GL11)))

(defn render-ball [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        dx (get-in ent [:size :x])
        dy (get-in ent [:size :y])
        [r g b] (get-in ent [:colour :rgb])]
    (GL11/glColor3f r g b)
    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x dx) y)
    (GL11/glVertex2f (+ x dx) (+ y dy))
    (GL11/glVertex2f x (+ y dy))
    (GL11/glEnd)))

(defn render-brick [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        dx (get-in ent [:size :x])
        dy (get-in ent [:size :y])
        [r g b] (get-in ent [:colour :rgb])
        destroyed? (get-in ent [:destroyed? :destroyed])]
    (if-not destroyed?
      (do (GL11/glColor3f r g b)
          (GL11/glBegin GL11/GL_QUADS)
          (GL11/glVertex2f x y)
          (GL11/glVertex2f (+ x dx) y)
          (GL11/glVertex2f (+ x dx) (+ y dy))
          (GL11/glVertex2f x (+ y dy))
          (GL11/glEnd)))))

; add wall renderer
(defn render-wall [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        dx (get-in ent [:size :x])
        dy (get-in ent [:size :y])
        [r g b] (get-in ent [:colour :rgb])] 
    (GL11/glColor3f r g b)
    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x dx) y)
    (GL11/glVertex2f (+ x dx) (+ y dy))
    (GL11/glVertex2f x (+ y dy))
    (GL11/glEnd)))

(defn render-paddle [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        dx (get-in ent [:size :x])
        dy (get-in ent [:size :y])
        [r g b] (get-in ent [:colour :rgb])]
    (GL11/glColor3f 0.78 0.28 0.28)
    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x dx) y)
    (GL11/glVertex2f (+ x dx) (+ y dy))
    (GL11/glVertex2f x (+ y dy))
    (GL11/glEnd)))
