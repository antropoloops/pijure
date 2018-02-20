(ns com.antropoloops.main
  (:require [com.antropoloops.system :as s]
            [com.stuartsierra.component :as c])
  (:gen-class))


(defn -main [& args]
  (println "STARTING!!" (c/start (s/example-system))))




