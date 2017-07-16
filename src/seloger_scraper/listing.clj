(ns seloger-scraper.listing
  (:require
   [sparkledriver.core :refer [fetch! find-by-id text]]))

(defn scrape [browser url]
  (println "scrape listing " url)
  (let [listing-page (fetch! browser url)]
    {:title (-> listing-page (find-by-id "js-titleAnnonce") text)
     :description (-> listing-page (find-by-id "js-descriptifBien") text)}))


