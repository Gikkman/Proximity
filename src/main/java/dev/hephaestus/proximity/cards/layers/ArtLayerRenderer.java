package dev.hephaestus.proximity.cards.layers;

import dev.hephaestus.proximity.templates.layers.ImageLayer;
import dev.hephaestus.proximity.util.Result;
import dev.hephaestus.proximity.util.StatefulGraphics;
import dev.hephaestus.proximity.xml.LayerRenderer;
import dev.hephaestus.proximity.xml.RenderableCard;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ArtLayerRenderer extends LayerRenderer {
    @Override
    public Result<Optional<Rectangle2D>> renderLayer(RenderableCard card, RenderableCard.XMLElement element, StatefulGraphics graphics, Rectangle2D wrap, boolean draw, float scale, Rectangle2D bounds) {
        if (!element.hasAttribute("width") && !element.hasAttribute("height")) {
            return Result.error("Image layer must have either 'width' or 'height' attribute");
        }

        int x = (element.hasAttribute("x") ? Integer.decode(element.getAttribute("x")) : 0);
        int y = (element.hasAttribute("y") ? Integer.decode(element.getAttribute("y")) : 0);
        Integer width = element.hasAttribute("width") ? Integer.decode(element.getAttribute("width")) : null;
        Integer height = element.hasAttribute("height") ? Integer.decode(element.getAttribute("height")) : null;

        try {
            String fileLocation = card.getAsString("image_uris", "art_crop");

            return Result.of(Optional.ofNullable(new ImageLayer(
                    element.getId(),
                    x,
                    y,
                    ImageIO.read(new URL(fileLocation)),
                    width,
                    height,
                    fileLocation).draw(graphics, wrap, draw, scale)
            ));
        } catch (IOException e) {
            return Result.error("Failed to create layer '%s': %s", element.getId(), e.getMessage());
        }
    }
}