(ns com.antropoloops.o-osc
    (:require [com.antropoloops.osc :as ao]
              [com.stuartsierra.component :as component]
              [com.antropoloops.overtone :as overtone]
              [overtone.osc :as osc])
)
;; https://github.com/overtone/osc-clj
#_(defn close [what]
  (try
    (.close what)
    (catch Exception e (println :closing-exception e))
    ))

(defrecord OSCServer [path in-port out-port]
  component/Lifecycle
  (start [component]
    (println ";; starting osc server" )
    (let [receiver (osc/osc-server in-port)

          sender (osc/osc-client "localhost" out-port)
          res (assoc component
                     :receiver receiver
                     :sender sender)
          listener (fn [message]
                     (let [args  (:args message)]
                       (println args)
                       (println :listener-args!  args  (= 5 (first args)))
                       (when (= 5 (first args)) (overtone/play (:overtone component) "jau"))
                       (println (type  (first args)) args)))]
      (osc/osc-handle receiver path listener)
      res))

  (stop [component]
    (println ";; Stopping osc server")
    (osc/osc-rm-handler (:receiver component) path)
    (osc/osc-close (:sender component))
    (osc/osc-close (:receiver component))
    component)

  ao/OSC
  (send-message [component path mensaje]
    (osc/osc-send (:sender component) path mensaje)))


(defn new* [path in-port out-port] (OSCServer. path in-port out-port))

(comment
  (def s (component/start (OSCServer. ao/path ao/in-port ao/out-port)))
  (component/stop s)
  (ao/send-message s ao/path 5)
  (def flute (sample "./resources/sample.wav"))
  (flute)
  (c/kill 41)

  )
