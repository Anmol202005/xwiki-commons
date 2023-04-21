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
package org.xwiki.test.internal;

import org.xwiki.component.descriptor.DefaultComponentDescriptor;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.configuration.internal.MemoryConfigurationSource;

/**
 * Mock {@link ConfigurationSource} that returns an empty list of configuration sources.
 *
 * @version $Id$
 * @since 1.6M2
 */
public class MockConfigurationSource extends MemoryConfigurationSource
{
    public static DefaultComponentDescriptor<ConfigurationSource> getDescriptor(String roleHint)
    {
        DefaultComponentDescriptor<ConfigurationSource> descriptor = new DefaultComponentDescriptor<>();
        descriptor.setRoleType(ConfigurationSource.class);
        if (roleHint != null) {
            descriptor.setRoleHint(roleHint);
        }
        descriptor.setImplementation(MockConfigurationSource.class);

        // Default to very high priority for retro compatibility reason (calling this method used to always register
        // the component without caring about any kind of priority and changing that behavior might affect a lot of unit
        // tests
        descriptor.setRoleHintPriority(0);

        return descriptor;
    }
}
