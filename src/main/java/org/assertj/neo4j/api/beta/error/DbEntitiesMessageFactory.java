package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.util.Strings;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DbEntitiesMessageFactory<E extends DbEntity<E>, EXPECTED, ITEM> extends BasicErrorMessageFactory {

    /**
     * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
     */
    protected DbEntitiesMessageFactory(final RecordType recordType, final String title, final String but,
                                       final List<E> actual, final EXPECTED expected, final List<ITEM> items,
                                       final ItemMessageFactory<EXPECTED, ITEM> factory) {
        super(
                "%nExpecting %s:%n"
                + "  %s%n"
                + "%s:%n"
                + "  %s %n"
                + "%s:%n%n"
                + Strings.escapePercent(describeItems(expected, items, factory)),
                unquotedString(recordType.pluralForm()),
                Entities.outputIds(actual),
                unquotedString(title),
                expected,
                unquotedString(but)
        );
    }

    private static <EXPECTED, ELEMENT> String describeItems(
            final EXPECTED expected, final List<ELEMENT> items,
            final ItemMessageFactory<EXPECTED, ELEMENT> factory) {
        return items.stream()
                .map(item -> factory.describeItem(expected, item))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    @FunctionalInterface
    interface ItemMessageFactory<EXPECTED, ITEM> {

        String describeItem(EXPECTED expected, ITEM item);

    }

}
