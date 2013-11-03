var userCoord, tempMarker;
var buttonStatus = false;

var map = L.map('map').setView([5, 5], 4);
        L.tileLayer('cmap/{z}/{x}/{y}.png', {
            minZoom: 2,
            maxZoom: 5,
            attribution: 'PolyFinder',
			continuousWorld: true,
			unloadInvisibleTiles: true,
            tms: true
        }).addTo(map);


//setbounds
var southWest = new L.LatLng(-85.02, -178.24),
    northEast = new L.LatLng(-.75, 31.64),
    bounds = new L.LatLngBounds(southWest, northEast);
map.setMaxBounds(bounds);
map.panTo(new L.LatLng(-65.737, -73.923));

// Firebase stuff
var polyfinderData = new Firebase('https://polyfindertest.firebaseio.com/events');
polyfinderData.on('value', function(snapshot) {
	console.log(snapshot.val());
	var events = snapshot.val();
	for (var i = 0; i < events.length; i++) {
		createEvent(events[i].message, new L.LatLng(events[i].coordinates.x, events[i].coordinates.y), events[i].id);
	}
});

map.on('click', onMapClick);  

//Add Button
var AddButton = L.Control.extend({
    options: {
        position: 'topright'
    },

    onAdd: function (map) {
        // create the control container with a particular class name
        var container = L.DomUtil.create('div', 'addButton');

        // ... initialize other DOM elements, add listeners, etc.
        container.addEventListener('click', function(e) {
        	// display buttons
        	if (!buttonStatus) {
        		buttonStatus = true;
        		map.addControl(new GButton());
        		map.addControl(new RButton());
        	}
        	
        	// add event
   
        });

        return container;
    }
});

map.addControl(new AddButton());

//Green Button
var GButton = L.Control.extend({
    options: {
        position: 'topright'
    },

    onAdd: function (map) {
        // create the control container with a particular class name
        var container = L.DomUtil.create('div', 'green');

        // ... initialize other DOM elements, add listeners, etc.
        container.addEventListener('click', function(e) {
        	console.log('test2');
        	var tempMarker = L.marker(e.latlng).addTo(map);
        	tempMarker.bindPopup("test");
       });
        
        return container;
    }
});


//Red Button
var RButton = L.Control.extend({
    options: {
        position: 'topright'
    },

    onAdd: function (map) {
        // create the control container with a particular class name
        var container = L.DomUtil.create('div', 'red');

        // ... initialize other DOM elements, add listeners, etc.
         container.addEventListener('click', function(e) {
        	console.log('red');
       });

        return container;
    }
});

function createEvent(txt, lat, id){
	var marker = L.marker(lat).addTo(map);
	marker.bindPopup(txt);
    marker.eventID = id;
    console.log(marker);
}

// Get coordinates
//-1 - top, -179 - left, 31 - right, -89 - bottom  
// b8: (-44.80912, -121.37695), b94: (-41.40978, -106.30371)
function onMapClick(e) {
   if (buttonStatus) {
   		console.log(e.latlng);
   		if (tempMarker != null) {
   			map.removeLayer(tempMarker);
   		}
   		tempMarker = L.marker(e.latlng).addTo(map);
       	tempMarker.bindPopup("test");
    }
}
