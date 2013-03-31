(ns breakout.core
    (:import (org.lwjgl LWJGLException)
     (org.lwjgl.opengl Display DisplayMode GL11)
     (org.lwjgl.input Keyboard)))

(def world-size [8 5])
(def screen-size [800 600])

(def PI 3.14159)
(def -PI -3.14159)

(defrecord World [bricks ball paddle running])
(defrecord Brick [colour destroyed])
(defrecord Paddle [position velocity])
(defrecord Ball [position velocity])

(def boundaries
     [])

(defn init-paddle []
  (->Paddle [350 580] [0 0]))

(defn init-ball []
  (->Ball [400 400] [-1 1]))

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
    (GL11/glVertex2f (+ x 100) y)
    (GL11/glVertex2f (+ x 100) (+ y 60))
    (GL11/glVertex2f x (+ y 60))
    (GL11/glEnd)))

(defn random-world []
   (->World (random-bricks) (init-ball) (init-paddle) true))

(defn draw-world [world]
  (let [[x y] world-size
        [gx gy] screen-size
        bx 100
        by 60]
    (doseq [row (range 0 y)]
      (doseq [col (range 0 x)]
        (draw-brick (nth (nth (:bricks world) row) col)
                    [(* col bx) (* row by)])))))

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

(defn draw [world]
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
  (let [paddle (:paddle world)
        ball (:ball world)]
    (draw-world world)
    (draw-ball ball)
    (draw-paddle paddle))
  (Display/update)
  (Display/sync 60))

(defn vadd [v1 v2]
  (let [[a b] v1
        [c d] v2]
    [(+ a c) (+ b d)]))

(defn update-input [state]
  (let [paddle (:paddle state) ]
    (cond (Keyboard/isKeyDown (Keyboard/KEY_LEFT))
              (assoc-in state [:paddle :velocity] [-1 0])
          (Keyboard/isKeyDown (Keyboard/KEY_RIGHT))
              (assoc-in state [:paddle :velocity] [1 0])
          (Keyboard/isKeyDown (Keyboard/KEY_ESCAPE))
              (assoc-in state [:running] false)
          true state))) 

(defn reset-velocity [state kw]
  (assoc-in state [kw :velocity] [0 0]))

(defn update-paddle [state]
  (let [paddle (:paddle state)
        position (vadd (:position paddle) (:velocity paddle))]
    (reset-velocity (assoc-in state [:paddle :position] position) :paddle)))

(defn update-ball [state]
  (let [ball (:ball state)
        position (vadd (:position ball) (:velocity ball))]
    (assoc-in state [:ball :position] position)))

(defn update-bricks [state]
  state)

(defn update [world]
  (-> world
      (update-input)
      (update-paddle)
      (update-ball)
      (update-bricks)))

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
