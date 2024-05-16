/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.extension.repository.maven.internal.converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.maven.model.Model;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.util.DefaultParameterizedType;
import org.xwiki.extension.Extension;
import org.xwiki.extension.maven.ArtifactModel;
import org.xwiki.extension.maven.internal.MavenExtension;
import org.xwiki.extension.maven.internal.MavenUtils;
import org.xwiki.extension.maven.internal.converter.AbstractModelConverter;
import org.xwiki.properties.converter.ConversionException;
import org.xwiki.properties.converter.Converter;

/**
 * Create an {@link Extension} from a Maven {@link Model}.
 * 
 * @version $Id$
 * @since 9.0RC1
 */
@Component
@Singleton
public class ArtifactModelConverter extends AbstractModelConverter<ArtifactModel>
{
    /**
     * The role of the component.
     */
    public static final ParameterizedType ROLE =
        new DefaultParameterizedType(null, Converter.class, ArtifactModel.class);

    @Inject
    private ExtensionTypeConverter converter;

    @Override
    public <G> G convert(Type targetType, Object sourceValue)
    {
        if (targetType == Extension.class) {
            try {
                return (G) convertToExtension((ArtifactModel) sourceValue);
            } catch (ComponentLookupException e) {
                throw new ConversionException("Failed to convert the ArtifactModel", e);
            }
        } else {
            throw new ConversionException(String.format("Unsupported target type [%s]", targetType));
        }
    }

    private MavenExtension convertToExtension(ArtifactModel artifactModel) throws ComponentLookupException
    {
        String groupId = MavenUtils.resolveGroupId(artifactModel.getModel());
        String artifactId = artifactModel.getModel().getArtifactId();
        String classifier = artifactModel.getClassifier();
        String type = artifactModel.getType() != null ? this.converter.mavenTypeToExtensionType(artifactModel.getType())
            : this.converter.mavenPackagingToExtensionType(artifactModel.getModel().getPackaging());
        String version = MavenUtils.resolveVersion(artifactModel.getModel());

        return convertToExtension(artifactModel.getModel(), groupId, artifactId, classifier, type, version);
    }
}
