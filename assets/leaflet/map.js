var map = L.map('map').setView([5, 5], 2);
        L.tileLayer('cmap/{z}/{x}/{y}.png', {
            minZoom: 2,
            maxZoom: 5,
            attribution: 'PolyFinder',
			continuousWorld: true,
			unloadInvisibleTiles: true,
            tms: true
        }).addTo(map);
	map.panTo(new L.LatLng(-65.737, -73.923));

function createEvent(txt, lat, id){
	var marker = L.marker(lat).addTo(map);
	marker.bindPopup(txt);
        marker.eventID = id;
}

