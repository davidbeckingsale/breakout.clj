(ns breakout.core
  (:import (org.lwjgl LWJGLException)
           (org.lwjgl.opengl Display DisplayMode GL11)
           (org.lwjgl.input Keyboard)))

(def world-size [8 5])
(def screen-size [800 600])

(def PI 3.14159)
(def -PI -3.14159)

(defrecord World [bricks ball paddle running])
(defrecord Brick [position colour destroyed])
(defrecord Paddle [position velocity])
(defrecord Ball [position velocity])

(defn init-paddle []
  (->Paddle [350 580] [0 0]))

(defn init-ball []
  (->Ball [400 400] [-1 1]))

(def bricks
  [(->Brick nil [1.0 0.0 0.0] nil)    ; red
   (->Brick nil [0.0 1.0 0.0] nil)    ; green
   (->Brick nil [0.0 0.0 1.0] nil)    ; blue
   (->Brick nil [1.0 0.5 0.0] nil)    ; orange
   (->Brick nil [1.0 0.0 1.0] nil)    ; purple
   (->Brick nil [0.0 1.0 1.0] nil)])  ; violet

(defn random-bricks []
  (let [[cols rows] world-size
        [bx by] [100 60]]
    ;    (letfn [(random-brick [] (bricks (rand-nth [:red :green :blue :orange :purple :violet])))
    ;            (random-row [] (vec (repeatedly cols random-brick)))]
    ;      (vec (repeatedly rows random-row)))))
    (for [row (range 0 rows)]
      (for [col (range 0 cols)]
        (let [x (* bx col)
              y (* by row)]
          (assoc (rand-nth bricks) :position [x y]))))))



(defn draw-brick [brick]
  (let [[screen-x screen-y] screen-size
        [x y] (:position brick)
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

(defn vmin [[x1 y1 :as v1] [x2 y2 :as v2]]
  (if (and (<= x1 x2)
           (<= y1 y2))
    v1
    v2))

(defn vmax [[x1 y1 :as v1] [x2 y2 :as v2]]
  (if (and (>= x1 x2)
           (>= y1 y2))
    v1
    v2))

(defn update-input [state]
  (let [paddle (:paddle state) ]
    (cond (Keyboard/isKeyDown (Keyboard/KEY_LEFT))
          (assoc-in state [:paddle :velocity] [-4 0])
          (Keyboard/isKeyDown (Keyboard/KEY_RIGHT))
          (assoc-in state [:paddle :velocity] [4 0])
          (Keyboard/isKeyDown (Keyboard/KEY_ESCAPE))
          (assoc-in state [:running] false)
          true state))) 

(defn reset-velocity [state kw]
  (assoc-in state [kw :velocity] [0 0]))

(defn update-paddle [state]
  (let [paddle (:paddle state)
        position (vmax [0 580] (vmin [700 580] (vadd (:position paddle) (:velocity paddle))))]
    (reset-velocity (assoc-in state [:paddle :position] position) :paddle)))

(defn reflect-ball-boundaries [state]
  (let [ball (:ball state)
        [x y] (:position ball)
        [vx vy] (:velocity ball)]
    (cond (<= x 0)
          (assoc state :ball (assoc ball :velocity [(- vx) vy]))
          (>= x 800)
          (assoc state :ball (assoc ball :velocity [(- vx) vy]))
          (<= y 0)
          (assoc state :ball (assoc ball :velocity [vx (- vy)]))
          (>= y 600)
          (assoc state :ball (assoc ball :velocity [vx (- vy)]))
          :else
          state)))

(defn reflect-ball-paddle [state]
  (let [paddle (:paddle state)
        [px py] (:position paddle)
        ball (:ball state)
        [bx by] (:position ball)
        [vx vy] (:velocity ball)]
    (if (and (>= by py)
             (>= bx px)
             (<= bx (+ 100 px)))
      (assoc state :ball (assoc ball :velocity [vx (- vy)]))
      state)))

(defn get-brick [bricks x y]
  (get-in bricks [y x]))

;(defn reflect-ball-bricks [state]
;  (let [bricks (:bricks state)
;        ball (:ball state)
;        [bx by] (:position ball)
;        [vx vy] (:velocity ball)]
;    (doseq [row (range (count bricks))]
;      (doseq [col (range (count (first bricks)))]
;          (if (collision? ball get-brick? state)
;            ;update with destroyed brick
;            ;update with normal brick
;            )))))

(defn update-ball [state]
  (let [ball (:ball state)
        position (vadd (:position ball) (:velocity ball))
        ball (assoc ball :position position)]
    (assoc state :ball ball)))

(defn update-bricks [state]
  state)

(defn update-physics [state]
  state
  (->> state
       reflect-ball-boundaries
       reflect-ball-paddle))

(defn update [world]
  (-> world
      (update-input)
      (update-physics)
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
