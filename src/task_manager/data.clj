(ns task-manager.data
  (:use [datomic.api :only [q db] :as d]))

(def uri "datomic:mem://tasks")
(defn dbval [] (db conn))

(defn install []
  (let [schema-tx (read-string (slurp "resources/schema.edn"))]
    (d/create-database uri)
    (d/transact conn schema-tx)))

(defn seed []
  (let [data-tx (read-string (slurp "resources/seed.dtm"))]
    (d/transact (d/connect uri) data-tx)))


(defn tasks []
  (let [results (q '[:find ?e :where [?e :task/number]] (dbval))]
    (map (fn [eid]
           (let [ent (d/entity (dbval) (first eid))]
             (d/touch ent)))
         results)))
