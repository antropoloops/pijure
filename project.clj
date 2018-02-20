(defproject com.antropoloops/juanpi "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.illposed.osc/javaosc-core "0.4"]
                 [com.1stleg/jnativehook "2.1.0"]
                 [overtone "0.10.3"]
                 ;; Logging
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/jcl-over-slf4j "1.7.12"]
                 [org.slf4j/jul-to-slf4j "1.7.12"]
                 [org.slf4j/log4j-over-slf4j "1.7.12"]
                 [ch.qos.logback/logback-classic "1.1.3" :exclusions [org.slf4j/slf4j-api]]

                 ]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [com.stuartsierra/component.repl "0.2.0"]]
                   :source-paths ["dev"]}}
;  :aot [com.antropoloops.main]
  :main ^{:skip-aot true} com.antropoloops.main

  )
