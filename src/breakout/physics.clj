(ns breakout.physics)

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

(defn intersect-ball-brick [ball brick]
  (let [[x y] (:position ball)
        [bx by] (:position brick)]
    (if (and (>= x bx) (<= x (+ bx 100)))
      (do (assoc brick :destroyed true))
      brick)))

(defn intersect-ball-row [ball row]
  (let [[x y] (:position ball)
        [_ by] (:position (first row))]
    (if (and (>= y by) (<= y (+ by 60)))
      (map #(intersect-ball-brick ball %) row)
      row)))

(defn reflect-ball-bricks [state]
  (let [ball (:ball state)
        bricks (:bricks state)]
    (assoc state :bricks
           (map #(intersect-ball-row ball %) bricks))))

(defn bounce-ball-brick [ball brick]
  (let [[x y] (:position ball)
        [vx vy] (:velocity ball)
        [bx by] (:position brick)]
    (if (and (>= x bx)
             (<= x (+ bx 100))
             (not (:destroyed brick)))
      (assoc ball :velocity [vx (- vy)])
      ball)))

(defn bounce-ball-row [ball row]
  (let [[x y] (:position ball)
        [_ by] (:position (first row))]
    (if (and (>= y by) (<= y (+ by 60)))
      (reduce bounce-ball-brick ball row)
      ball)))

(defn bounce-ball-bricks [state]
  (let [ball (:ball state)
        bricks (:bricks state)]
    (assoc state :ball (reduce bounce-ball-row ball bricks))))

(defn update-ball [state]
  (let [ball (:ball state)
        position (vadd (:position ball) (:velocity ball))
        ball (assoc ball :position position)]
    (assoc state :ball ball)))

(defn update-physics [state]
  state
  (->> state
       reflect-ball-boundaries
       reflect-ball-paddle
       bounce-ball-bricks
       reflect-ball-bricks
       update-paddle
       update-ball))
