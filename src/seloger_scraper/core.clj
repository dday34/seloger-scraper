(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http]
   [sparkledriver.core :refer [with-browser make-browser fetch! find-by-id text]])
  (:gen-class))

(def seloger-url "http://www.seloger.com/list.htm?tri=initial&idtypebien=2,1&pxmax=50000&idtt=2&ci=340172&naturebien=1,2,4")

(def seloger-url-listing "http://www.seloger.com/annonces/viagers/appartement/montpellier-34/boutonnet/114465593.htm?ci=340172&idtt=2&idtypebien=1,2&pxmax=50000&tri=initial#anchorBar_detail")

(defn get-listing-data []
  (with-browser [browser (fetch! (make-browser) seloger-url-listing)]
    (find-by-id browser "js-descriptifBien")))

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-page []
  (fetch-url seloger-url))

(defn get-link [l]
  {:title (-> l html/text string/trim)
   :link (-> l :attrs :href)})

(defn get-links []
  (map get-link (html/select (html/html-resource (:body (get-page))) [:div.title :a])))

(defn get-listing [url]
  (html/select (html/html-resource (:body (fetch-url url))) #{[:ul.criteres]}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
