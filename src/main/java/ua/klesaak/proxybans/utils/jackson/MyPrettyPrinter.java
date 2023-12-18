package ua.klesaak.proxybans.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;

import java.io.IOException;

/**
 * Спижжено отсюда:
 * <a href="https://stackoverflow.com/questions/64669932/how-to-configure-jackson-prettyprinter-format-json-as-gson">...</a>
 */

public class MyPrettyPrinter extends DefaultPrettyPrinter {

    public MyPrettyPrinter() {
        this._arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        this._objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
    }

    public MyPrettyPrinter(DefaultPrettyPrinter base) {
        super(base);
    }

    @Override
    public MyPrettyPrinter createInstance() {
        if (getClass() != MyPrettyPrinter.class) {
            throw new IllegalStateException("Failed `createInstance()`: " + getClass().getName()
                    + " does not override method; it has to");
        }
        return new MyPrettyPrinter(this);
    }

    @Override
    public MyPrettyPrinter withSeparators(Separators separators) {
        this._separators = separators;
        this._objectFieldValueSeparatorWithSpaces = separators.getObjectFieldValueSeparator() + " ";
        return this;
    }

    @Override
    public void writeEndArray(JsonGenerator g, int nrOfValues) throws IOException {
        if (!this._arrayIndenter.isInline()) {
            --this._nesting;
        }
        if (nrOfValues > 0) {
            this._arrayIndenter.writeIndentation(g, this._nesting);
        }
        g.writeRaw(']');
    }

    @Override
    public void writeEndObject(JsonGenerator g, int nrOfEntries) throws IOException {
        if (!this._objectIndenter.isInline()) {
            --this._nesting;
        }
        if (nrOfEntries > 0) {
            this._objectIndenter.writeIndentation(g, this._nesting);
        }
        g.writeRaw('}');
    }
}
