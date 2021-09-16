package com.zipper.api.kgmodule

import com.zipper.api.kgmodule.bean.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface KgTaskService {

    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 8002dddd",
        "KG-THash: 439de79",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @GET("musicsymbol/v1/system/profile")
    suspend fun profile(@QueryMap params:Map<String, String>): Response<KgTaskProfile>

    /**
     * userid=1882565136&token=h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57&appid=1005&from=client&dfid=-&signature=a19de0ce9a8f25d42b7b589c7bf2130f&mid=19866310614991365975980225640804152227&clientver=10829&clienttime=1630849668&uuid=532544371bb026cc684489e57eedbe9c&
     */
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 80023fff",
        "KG-THash: 697fce7",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @GET("musicsymbol/v1/task/sign_state")
    suspend fun signState(@QueryMap params:Map<String, String>): Response<KgTaskSignState>

    /**
     * ?srcappid=2919&clientver=10829&clienttime=1630849669476&mid=19866310614991365975980225640804152227&uuid=532544371bb026cc684489e57eedbe9c&dfid=-&userid=1882565136&token=h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57&appid=1005&from=client&spec=15&channel=musicsymbol10829&signature=0fb472334979c31729f26a9b2ffbb6dc
     */
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 8002d918",
        "KG-THash: 697fce7",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @POST("musicsymbol/v1/task/state_list")
    suspend fun stateList(@QueryMap params: Map<String, String>, @Body body: RequestBody): Response<KgTaskStateList>

    /**
     * userid=1882565136&token=h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57&appid=1005&from=client&spec=15&dfid=-&signature=f7ccffd6a1b6192a5bed8a9eb61e6704&mid=19866310614991365975980225640804152227&clientver=10829&clienttime=1630849663&uuid=532544371bb026cc684489e57eedbe9c&
     */
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 8002d102",
        "KG-THash: 697fce7",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @GET("musicsymbol/v1/user/info")
    suspend fun info(@QueryMap params: Map<String, String>): Response<KgTaskInfo>

    /**
     * userid=1882565136&token=h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57&appid=1005&from=client&dfid=-&mid=19866310614991365975980225640804152227&clientver=10829&clienttime=1630849673&uuid=532544371bb026cc684489e57eedbe9c&signature=9bc7a3a990b805411cbc2ee3bf4ad814&
     */
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 800247d9",
        "KG-THash: 697fce7",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @GET("musicsymbol/v1/gift/list")
    suspend fun list(@QueryMap params: Map<String, String>): Response<KgTaskList>

    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 8002b24f",
        "KG-THash: 22fd1ac",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi"
    )
    @POST("musicsymbol/v1/task/submit")
    suspend fun submit(@QueryMap params: Map<String, String>, @Body body: RequestBody): Response<KgTaskSubmit>

    /**
     * 酷狗任务签到
     * param {"code":"20210905"}
     */
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "KG-FAKE: 1882565136",
        "KG-RC: 1",
        "KG-RF: 0075da00",
        "KG-THash: 697fce7",
        "User-Agent: Android11-AndroidPhone-10829-14-0-MusicalNoteProtocol-wifi",
        "Content-Type: application/x-www-form-urlencoded"
    )
//    @POST("musicsymbol/v1/task/signon?srcappid=2919&clientver=10829&clienttime=1630851621967&mid=19866310614991365975980225640804152227&uuid=532544371bb026cc684489e57eedbe9c&dfid=-&userid=1882565136&token=h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57&appid=1005&from=client&signature=82e3a72797ea970556b1640dc7f240f7")
    @POST("musicsymbol/v1/task/signon")
    suspend fun signOn(
        @QueryMap params:Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<KgTaskSignon>
}