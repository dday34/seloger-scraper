(ns seloger-scraper.listing
  (:require
   [clojure.data.json :as json]
   [sparkledriver.core :refer [fetch! find-by-id find-by-class text]]
   [clojure.tools.logging :as log]))

(defn scrape [browser output-file-name url]
  (log/info "Scrape listing" url)
  (let [listing-page (fetch! browser url)
        listing {:title (-> listing-page (find-by-class "detail-title") text)
                 :description (-> listing-page (find-by-id "js-descriptifBien") text)}]
    (spit output-file-name (json/write-str listing) :append true)
    listing))


