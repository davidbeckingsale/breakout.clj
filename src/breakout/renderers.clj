(ns breakout.renderers
  (:use [breakout.lib.macros :only (?)]
        [breakout.lib.core :only (first-e)])
  (:import (org.lwjgl.opengl Display GL11)
           (org.newdawn.slick Color)
           (org.newdawn.slick.opengl Texture TextureLoader)
           (org.newdawn.slick.util ResourceLoader)))

(def font
  {\0 {:row 2 :col 0}
   \1 {:row 2 :col 1}
   \2 {:row 2 :col 2}
   \3 {:row 2 :col 3}
   \4 {:row 2 :col 4}
   \5 {:row 2 :col 5}
   \6 {:row 2 :col 6}
   \7 {:row 2 :col 7}
   \8 {:row 3 :col 0}
   \9 {:row 3 :col 1}
   \a {:row 4 :col 1}
   \b {:row 4 :col 2}
   \c {:row 4 :col 3}
   \d {:row 4 :col 4}
   \e {:row 4 :col 5}
   \f {:row 4 :col 6}
   \g {:row 4 :col 7}
   \h {:row 5 :col 0}
   \i {:row 5 :col 1}
   \j {:row 5 :col 2}
   \k {:row 5 :col 3}
   \l {:row 5 :col 4}
   \m {:row 5 :col 5}
   \n {:row 5 :col 6}
   \o {:row 5 :col 7}
   \p {:row 6 :col 0}
   \q {:row 6 :col 1}
   \r {:row 6 :col 2}
   \s {:row 6 :col 3}
   \t {:row 6 :col 4}
   \u {:row 6 :col 5}
   \v {:row 6 :col 6}
   \w {:row 6 :col 7}
   \x {:row 7 :col 0}
   \y {:row 7 :col 1}
   \z {:row 7 :col 2}
   \. {:row 1 :col 6}
   \< {:row 3 :col 4}
   \> {:row 3 :col 6}
   \! {:row 0 :col 1}
   \space {:row 0 :col 0}
   \: {:row 3 :col 2}
   :font (delay (TextureLoader/getTexture "PNG" (ResourceLoader/getResourceAsStream "assets/null_terminator.png")))
   :width 40
   :height 40})

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
  (let [texture @(get font :font)
        xo (get-in ent [:position :x])
        yo (get-in ent [:position :y])
        score-string (format "%d" (get-in ent [:game :score]))]
    (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MAG_FILTER, GL11/GL_NEAREST)
    (. (Color/white) bind)
    (. texture bind)
    (doseq [[c xoffset] (map vector 
                          (seq score-string) 
                          (range (- (count score-string) 1) -1 -1))]
      (let [ texcoords (get font c) 
            char-width (/ 1.0 8)
            txmin (* (:col texcoords) char-width)
            txmax (+ txmin char-width) 
            char-height (/ 1.0 8)
            tymin (* (:row texcoords) char-height)
            tymax (+ tymin char-height)
            x (- xo (* xoffset (get font :width)))]
        (GL11/glBegin GL11/GL_QUADS)
        (GL11/glTexCoord2f txmin tymin)
        (GL11/glVertex2f x yo)
        (GL11/glTexCoord2f txmax tymin)
        (GL11/glVertex2f (+ x (get font :width)) yo)
        (GL11/glTexCoord2f txmax tymax)
        (GL11/glVertex2f (+ x (get font :width)) (+ yo (get font :height)))
        (GL11/glTexCoord2f txmin tymax)
        (GL11/glVertex2f x (+ yo (get font :height)))
        (GL11/glEnd)))
    (GL11/glBlendFunc GL11/GL_ONE GL11/GL_ZERO)))

(defn render-text [ent]
  (let [texture @(get font :font)
        xo (get-in ent [:position :x])
        yo (get-in ent [:position :y])
        string (get-in ent [:text :string])
        size (get-in ent [:text :size])]
    (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MAG_FILTER, GL11/GL_NEAREST)
    (. (Color/white) bind)
    (. texture bind)
    (GL11/glColor3f 1.0 1.0 1.0)
    (doseq [[c xoffset] (map vector 
                          (seq string) 
                          (range (count string)))]
      (let [texcoords (get font c) 
            char-width (/ 1.0 8)
            txmin (* (:col texcoords) char-width)
            txmax (+ txmin char-width) 
            char-height (/ 1.0 8)
            tymin (* (:row texcoords) char-height)
            tymax (+ tymin char-height)
            x (+ xo (* xoffset size))]
        (GL11/glBegin GL11/GL_QUADS)
        (GL11/glTexCoord2f txmin tymin)
        (GL11/glVertex2f x yo)
        (GL11/glTexCoord2f txmax tymin)
        (GL11/glVertex2f (+ x size) yo)
        (GL11/glTexCoord2f txmax tymax)
        (GL11/glVertex2f (+ x size) (+ yo size))
        (GL11/glTexCoord2f txmin tymax)
        (GL11/glVertex2f x (+ yo size))
        (GL11/glEnd)))
    (GL11/glBlendFunc GL11/GL_ONE GL11/GL_ZERO)))

(defn render-lives [ent]
  (let [x (get-in ent [:position :x])
        y (get-in ent [:position :y])
        dx (get-in ent [:size :x])
        dy (get-in ent [:size :y])
        [r g b] [0.78 0.28 0.28]
        lives (? (first-e :game) :game :lives)]
    (doseq [o (range lives)]
      (let [xo (+ x (* 20 o))]
        (GL11/glColor3f r g b)
        (GL11/glBegin GL11/GL_QUADS)
        (GL11/glVertex2f xo y)
        (GL11/glVertex2f (+ xo dx) y)
        (GL11/glVertex2f (+ xo dx) (+ y dy))
        (GL11/glVertex2f xo (+ y dy))
        (GL11/glEnd)))))
