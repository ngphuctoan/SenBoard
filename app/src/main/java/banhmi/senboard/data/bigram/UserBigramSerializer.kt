package banhmi.senboard.data.bigram

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import banhmi.senboard.datastore.snippets.proto.UserBigram
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserBigramSerializer : Serializer<UserBigram> {
    override val defaultValue: UserBigram = UserBigram.getDefaultInstance()

    override suspend fun readFrom(
        input: InputStream,
    ): UserBigram {
        try {
            return UserBigram.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserBigram,
        output: OutputStream,
    ) {
        return t.writeTo(output)
    }
}