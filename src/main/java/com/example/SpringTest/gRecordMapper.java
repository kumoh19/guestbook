package com.example.SpringTest;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface gRecordMapper {
	// 데이터베이스와 객체를 연동시키는 SQL맵핑
		//글저장
		@Insert("INSERT INTO guestbook_t (title, content, writer) VALUES ( #{title}, #{content}, #{writer} )")
		void writeRecord(@Param("title") String title, 
								@Param("content") String content,
								@Param("writer") String writer);
		
//		@Delete("DELETE FROM member_t WHERE m_name= #{m_name}")
//		void deleteMemberInfo(@Param("m_name") String name);
		//글 1개 가져오기
		@Select("SELECT * FROM guestbook_t WHERE no= #{no}")
		gRecord getRecord(@Param("no") int no);
		//전체 글 가져오기
		@Select("SELECT * FROM guestbook_t")
		ArrayList<gRecord> getAllRecords();
		
		@Select("SELECT * FROM login_t WHERE m_id= #{m_id} and m_pw= #{m_pw}")
		gUserInfo getUserInfo(@Param("m_id") String m_id, @Param("m_pw") String m_pw);
}
