(defproject task-manager "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.2.0-beta2"]
                 [ring/ring-json "0.2.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [com.datomic/datomic-free "0.8.3889"]
                 [org.clojure/clojurescript "0.0-971"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler task-manager.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :main task-manager.server)


