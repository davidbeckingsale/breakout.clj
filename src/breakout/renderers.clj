(ns breakout.renderers
  (:import (org.lwjgl.opengl Display GL11)
           (org.newdawn.slick Color)
           (org.newdawn.slick.opengl Texture TextureLoader)
           (org.newdawn.slick.util ResourceLoader)))

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
        [r g b] (get-in ent [:colour :rgb]) ]
    (GL11/glColor3f r g b)
    (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x dx) y)
    (GL11/glVertex2f (+ x dx) (+ y dy))
    (GL11/glVertex2f x (+ y dy))
    (GL11/glEnd)))

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

(defn render-score [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        ;score-string (get-in ent [:game :score])
        texture (get-in ent [:texture :texture])]
    (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
    (. (Color/white) bind)
    (. texture bind)
      (GL11/glBegin GL11/GL_QUADS)
      (GL11/glTexCoord2f 0 0)
      (GL11/glVertex2f x y)
      (GL11/glTexCoord2f 1 0)
      (GL11/glVertex2f (+ x (. texture getTextureWidth)) y)
      (GL11/glTexCoord2f 1 1)
      (GL11/glVertex2f (+ x (. texture getTextureWidth)) (+ y (. texture getTextureHeight)))
      (GL11/glTexCoord2f 0 1)
      (GL11/glVertex2f x (+ y (. texture getTextureHeight)))
      (GL11/glEnd)
    (GL11/glBlendFunc GL11/GL_ONE GL11/GL_ZERO)))

; (defn render-text [ent]
;   (let [txt (get-in ent [:text :string])
;         x (get-in ent [:position :x])
;         y (get-in ent [:position :y])]

