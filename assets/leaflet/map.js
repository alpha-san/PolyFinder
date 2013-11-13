/**
 * userCoord and tempMarker are variables used to create a marker
 * that the user puts onto the map.
 */
var userCoord, tempMarker; 

/**
 *  nextId is used to keep track of current nextId for the events.
 * The id is retrieved from the last event in the firebase.
 */
var nextId;

/**
 * user_id is a variable that is used to keep track of the current user
 * using PolyFinder. The user_id is retrieved from firebase using the 
 * security javascript file and the auth variable.
 */
var user_id = 0;

/** Markers is a variable used to keep track of all the markers.
 * It is an array with all the markers. It is used to delete events
 * and update events as they are in the firebase.
 */
var markers = new Array();

/**
 * email and password is used for logging the user into PolyFinder's Firebase.
 * For testing purposes, the email and password are hardcoded below.
 */
var email = 'rltan@csupomona.edu', password = 'gay';


/**
 * map is a variable that holds the Cal Poly map.
 * It is an object used from leaflet.
 */
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
var polyfinderData = new Firebase('https://polyfindertest.firebaseio.com/');
polyfinderData.on('child_added', function(snapshot) {
	var c_event = snapshot.val();
	createEvent(c_event.message, new L.LatLng(c_event.coordinates.x, c_event.coordinates.y), c_event.id);
	nextId = c_event.id;
});
polyfinderData.on('child_removed', function(snapshot) {
	// remove events from the map
	var id_to_remove = snapshot.val().id;
	for (var i = 0; i < markers.length; i++) {
		if (markers[i].eventID == id_to_remove) {
			map.removeLayer(markers[i]);
			break;
		}
	}
});
polyfinderData.on('child_changed', function(snapshot) {
	// updates to events will show on map
	var u_event = snapshot.val();
	alert('attempting to update event');
	for (var i = 0; i < markers.length; i++) {
		if (markers[i].eventID == u_event.id) {
			map.removeLayer(markers[i]);
			// optimize so that we don't have to create a new event each time
			createEvent(u_event.message, new L.LatLng(u_event.coordinates.x, u_event.coordinates.y), u_event.id);
			alert('event updated!');
			break;
			markers.splice(i, 1);
		}
	}

});

var auth = new FirebaseSimpleLogin(polyfinderData, function(error, user) {
	if (error) {
		// an error occured while attempting to login
		console.log(error);
		alert('NOT LOGGED IN! :(');
	} else if (user) {
		// user is authenticated
		console.log('User ID: ' + user.id + ', Provider: ' + user.provider);
		console.log(user);
		alert('Logged In!');
		user_id = user.uid;
	} else {
        // user is logged out
    }
});

// login user
user_login(email, password);

function user_login(email, pwd) {
	// login
	auth.login('password', {
		email: email,
		password: pwd,
	});
}

function addButton(){
	map.on('click', onMapClick); 
}

function yesButton(type, content){
	map.off('click', onMapClick);
	//tempMarker.content = content;
	//tempMarker.type = type;
	//tempMarker.addEventListener('click', function(){WebViewInterface.eventDialog('User', content);});
	// add to firebase
	addTestEvent('Cloud 9', content, tempMarker.getLatLng());

	tempMarker = null;
	//alert(tempMarker);
}

function noButton(){
	if (tempMarker != null) {
		map.removeLayer(tempMarker);
	}
	map.off('click', onMapClick);
}


function createEvent(txt, lat, id){
	var marker = L.marker(lat).addTo(map);
	marker.addEventListener('click', function(){WebViewInterface.eventDialog('User', txt);});
	//marker.bindPopup(txt);
	marker.eventID = id;
	console.log(marker);
	markers.push(marker);
}

function addTestEvent(loc, mes, lat) {
	var c_event = new Object();
	c_event.date = JSON.stringify(new Date());
	c_event.location = loc;
	c_event.message = mes;
	c_event.postedBy = 'rltan';
	c_event.postedByUserId = user_id;
	c_event.rating = 0;
	c_event.comments = new Array();
	c_event.id = ++nextId;
	c_event.likedBy = new Array();
	c_event.coordinates = new Object();
	c_event.coordinates.x = lat.lat;
	c_event.coordinates.y = lat.lng;
	polyfinderData.push(c_event); 
}

// Get coordinates
//-1 - top, -179 - left, 31 - right, -89 - bottom  
// b8: (-44.80912, -121.37695), b94: (-41.40978, -106.30371)

function onMapClick(e) {
	console.log(e.latlng);
	if (tempMarker != null) {
		map.removeLayer(tempMarker);
	}
	tempMarker = L.marker(e.latlng).addTo(map);
	//tempMarker.bindPopup("Test");
}

function alertMe(){
	alert("Hey Cloud 9");
}