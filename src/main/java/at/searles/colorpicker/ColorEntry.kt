package at.searles.colorpicker

import androidx.recyclerview.widget.DiffUtil

class ColorEntry(val name: String, val rgb: Int) {

    object DiffCallback: DiffUtil.ItemCallback<ColorEntry>() {
        override fun areItemsTheSame(oldItem: ColorEntry, newItem: ColorEntry): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ColorEntry, newItem: ColorEntry): Boolean {
            return oldItem == newItem
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ColorEntry

        if (name != other.name) return false
        if (rgb != other.rgb) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + rgb
        return result
    }


}
