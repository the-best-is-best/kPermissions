package io.github.kpermissionslocationChecker

import kotlinx.coroutines.flow.Flow

expect val locationServiceEnabledFlow: Flow<Boolean>
