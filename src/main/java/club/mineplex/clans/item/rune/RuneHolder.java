package club.mineplex.clans.item.rune;

import lombok.NonNull;

import java.util.*;

/**
 * Implementation of Mineplex's attribute container stored within a {@link club.mineplex.clans.item.CustomItem}'s NBT data.
 */
public class RuneHolder {

	private RuneImpl _superPrefix = null;
	private RuneImpl _prefix = null;
	private RuneImpl _suffix = null;

	public Collection<RuneImpl> asSet()
	{
		final Set<RuneImpl> runes = new HashSet<>(Arrays.asList(_superPrefix, _prefix, _suffix));
		runes.removeIf(Objects::isNull);
		return runes;
	}

	public void add(@NonNull RuneImpl attribute) {
		switch (attribute.getType()) {
			case SUPER_PREFIX:
				setSuperPrefix(attribute);
				break;
			case PREFIX:
				setPrefix(attribute);
				break;
			case SUFFIX:
				setSuffix(attribute);
				break;
		}
	}

	public void setSuperPrefix(@NonNull RuneImpl rune) {
		this._superPrefix = rune;
	}

	public void setPrefix(@NonNull RuneImpl rune) {
		this._prefix = rune;
	}

	public void setSuffix(@NonNull RuneImpl rune) {
		this._suffix = rune;
	}

	public Optional<RuneImpl> getSuperPrefix() {
		return Optional.ofNullable(_superPrefix);
	}

	public Optional<RuneImpl> getPrefix() {
		return Optional.ofNullable(_prefix);
	}

	public Optional<RuneImpl> getSuffix() {
		return Optional.ofNullable(_suffix);
	}

}
