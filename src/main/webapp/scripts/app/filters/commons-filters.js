App.filter("duration", function() {
	return function(input) {
		var millis = input * 60 * 1000;
		return  (Math.floor(moment.duration(millis).asHours()) + 'h')+ ((moment.duration(millis).minutes() < 10) ? '0'+ moment.duration(millis).minutes()  : moment.duration(millis).minutes());
	};
});

App.filter("durationHours", function() {
	return function(input) {
		var millis = input * 60 * 1000;
		return moment.duration(millis).asHours()+'h';
	};
});

App.filter("dateFromNow", function() {
	return function(input) {
		return moment(input).from(moment());
	};
});