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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import io.jenetics.jpx.Link;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class RouteLinkDAO extends DAO {

	/**
	 * Represents a row in the "route_link" table.
	 */
	private static final class Row {
		final Long routeID;
		final Long linkID;

		Row(final Long routeID, final Long linkID) {
			this.routeID = routeID;
			this.linkID = linkID;
		}

		Long routeID() {
			return routeID;
		}

		Long linkID() {
			return linkID;
		}
	}

	public RouteLinkDAO(final Connection conn) {
		super(conn);
	}

	private static final RowParser<Row> RowParser = rs -> new Row(
		rs.getLong("route_id"),
		rs.getLong("link_id")
	);

	/* *************************************************************************
	 * SELECT queries
	 **************************************************************************/

	public Map<Long, List<Link>> selectLinksByRouteID(final List<Long> ids)
		throws SQLException
	{
		final String query =
			"SELECT route_id, link_id " +
			"FROM route_link " +
			"WHERE route_id IN ({ids})";

		final List<Row> rows = SQL(query)
			.on(Param.values("ids", ids))
			.as(RowParser.list());

		final Map<Long, Link> links = with(LinkDAO::new)
			.selectByVals(Column.of("id", Row::linkID), rows).stream()
			.collect(toMap(Stored::id, Stored::value, (a, b) -> b));

		return rows.stream()
			.map(row -> Pair.of(row.routeID, links.get(row.linkID)))
			.collect(groupingBy(Pair::_1, mapping(Pair::_2, toList())));
	}

	/* *************************************************************************
	 * INSERT queries
	 **************************************************************************/

	public List<Pair<Long, Long>> insert(final List<Pair<Long, Long>> routeLinks)
		throws SQLException
	{
		final String query =
			"INSERT INTO route_link(route_id, link_id) " +
			"VALUES({route_id}, {link_id});";

		Batch(query).execute(routeLinks, mdl -> asList(
			Param.value("route_id", mdl._1),
			Param.value("link_id", mdl._2)
		));

		return routeLinks;
	}

}