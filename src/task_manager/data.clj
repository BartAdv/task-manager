(ns task-manager.data
  (:use [datomic.api :only [q db] :as d])
  (:require clojure.java.io))

(def uri "datomic:mem://tasks")
(d/create-database uri)
(def conn (d/connect uri))
(defn dbval [] (db conn))

(def schema-tx (read-string (slurp (clojure.java.io/resource "schema.edn"))))
(d/transact conn schema-tx)

(def seed-tx (read-string (slurp (clojure.java.io/resource "seed.dtm"))))
(d/transact conn seed-tx)

(defn tasks []
  (->>
   (q '[:find ?e :where [?e :task/number]] (dbval))
   (map (fn [eid]
          (let [ent (d/entity (dbval) (first eid))]
            (d/touch ent))))
   (sort-by :task/number)))

(defn task! [task]
  (d/transact conn task))

(defn create-task! [desc]
  (d/transact conn [[:create-task desc]]))
