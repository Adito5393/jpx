/*
 * Java GPX Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.jpx.jdbc;

import static java.lang.String.format;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class RouteWayPoint {

	private final long _routeID;
	private final long _wayPointUD;

	private RouteWayPoint(final long routeID, final long wayPointUD) {
		_routeID = routeID;
		_wayPointUD = wayPointUD;
	}

	long getRouteID() {
		return _routeID;
	}

	long getWayPointUD() {
		return _wayPointUD;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash += 37*Long.hashCode(_routeID) + 31;
		hash += 37*Long.hashCode(_wayPointUD) + 31;

		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof RouteWayPoint &&
			Long.compare(((RouteWayPoint)obj)._routeID, _routeID) == 0 &&
			Long.compare(((RouteWayPoint)obj)._wayPointUD, _wayPointUD) == 0;
	}

	@Override
	public String toString() {
		return format("RouteWayPoint[%d, %d]", _routeID, _wayPointUD);
	}

	public static RouteWayPoint of(final long metadataID, final long linkID) {
		return new RouteWayPoint(metadataID, linkID);
	}

	public static RouteWayPoint of(final Pair<Long, Long> pair) {
		return new RouteWayPoint(pair._1, pair._2);
	}

}
